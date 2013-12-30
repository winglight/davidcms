package controllers;

import models.CompanyModel;
import models.ContentModel;
import models.dto.MessageModel;
import models.status.ContentLanguage;
import models.status.ContentType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.Constants;
import views.html.cms;
import views.html.intro;

import java.io.*;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

@Security.Authenticated(Secured.class)
public class CMS extends Controller {

    public static class Company {

        public String name;
        public String description;
        public String marquee;

        public String validate() {
            if (name == null || description == null) {
                return "Invalid name or description";
            }
            return null;
        }

    }

    public static class Content {

        public Long id;

        public String name;

        public String description;

        public String url;

        public String phoneNumber;

        public ContentType contentType;

        public String validate() {
            if (name == null) {
                return "Invalid name";
            }
            return null;
        }

    }

    public static Result index() {
        return ok(intro.render(null));
    }

    public static Result cms() {
        return ok(cms.render(null));
    }

    public static Result saveCompany() {
        Form<Company> loginForm = form(Company.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return ok(loginForm.errorsAsJson());
        } else {
            String name = loginForm.get().name;
            String desc = loginForm.get().description;
            String marquee = loginForm.get().marquee;
            CompanyModel cm = CompanyModel.save(name, desc, marquee);

            MessageModel<CompanyModel> mm = new MessageModel<CompanyModel>();
            mm.setFlag(true);
            mm.setData(cm);
            return ok(Json.toJson(mm));
        }
	}
    
    public static Result saveContent() {
        Form<Content> loginForm = form(Content.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return ok(loginForm.errorsAsJson());
        } else {
            final Long id = loginForm.get().id;
            ContentModel cm = null;
            if(id == null){
                cm = new ContentModel();
            }else{
                cm = ContentModel.find.byId(id);
            }
            cm.name = loginForm.get().name;
            cm.description = loginForm.get().description;
            cm.url = loginForm.get().url;
            cm.phoneNumber = loginForm.get().phoneNumber;
            cm.contentType = loginForm.get().contentType;

            if(id == null){
                cm.save();
            }else{
                cm.update();
            }

            MessageModel<ContentModel> mm = new MessageModel<ContentModel>();
            mm.setFlag(true);
            mm.setData(cm);
            return ok(Json.toJson(mm));
        }
	}
    public static Result deleteContent(Long cid) {
    	ContentModel.delete(cid);
		return ok(Constants.RETURN_SUCCESS);
	}

    public static Result uploadImage() {
        Http.MultipartFormData body = request().body().asMultipartFormData();

        Map map = body.asFormUrlEncoded();
        if(!map.containsKey("cid") || !map.containsKey("isBig")){
            return ok("error:bad parameters!");
        }
        Long cid;
        cid = Long.valueOf(((String[])map.get("cid"))[0]);
        Boolean isBig = Boolean.valueOf(((String[])map.get("isBig"))[0]);

        Http.MultipartFormData.FilePart imgFile = body.getFile("file");
        if (imgFile != null) {
            String path = Play.application().path().getPath() + "/upload/";
            String destFileName = String.valueOf(System.currentTimeMillis());

            String contentType = imgFile.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                return ok(Json.toJson("error:not apk file"));
            }

            File file = imgFile.getFile();
            try {
                FileUtils.copyFile(file, new File(path + destFileName));
                ContentModel.updateContentImage(cid, destFileName, isBig);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ok(destFileName);

        }
        return ok(Json.toJson("error:Missing file"));
    }
}

