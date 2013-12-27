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

import static play.data.Form.form;

public class CMS extends Controller {

    public static class Company {

        public String name;
        public String description;

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
            CompanyModel cm = CompanyModel.save(name, desc);

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

    public static Result uploadImage(Long cid, boolean isBig) {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart imgFile = body.getFile("files[]");
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
            // return redirect(routes.Courses.showImage(fileName));

        }
        return ok(Json.toJson("error:Missing file"));
    }
}

