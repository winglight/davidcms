package controllers;

import models.AppModel;
import models.CategoryModel;
import models.CompanyModel;
import models.UserModel;
import models.dto.MessageModel;
import models.dto.PageInfo;
import models.dto.SimpleAppModel;
import org.apache.commons.io.IOUtils;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.Constants;
import views.html.login;

import java.io.*;
import java.util.Date;
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

    public static Result getCompany() {
        CompanyModel cm = CompanyModel.find.findUnique();
    	
		MessageModel<CompanyModel> mm = new MessageModel<CompanyModel>();
		mm.setFlag(true);
		mm.setData(cm);
		return ok(Json.toJson(mm));
	}
    
    public static Result saveCompany() {
        Form<Company> loginForm = form(Company.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return ok(loginForm.errorsAsJson());
        } else {
            String name = loginForm.get().name;
            String desc = loginForm.get().description;
            CompanyModel cm = CompanyModel.save(name, desc);

            return ok(Json.toJson(cm));
        }
	}
    
    public static Result getContentsByType(Long category) {
    	List<SimpleAppModel> list = AppModel.findAppsByCategory(category, page.intValue(), (lastUpdateDate > 0?new Date(lastUpdateDate):null));
    	PageInfo pageInfo = AppModel.getHotAppsPageInfo(page.intValue());
    	if(list.size() < Constants.AMOUNT_PER_PAGE){
    		pageInfo.setEnd(list.size());
    	}
    	
		MessageModel<List<SimpleAppModel>> mm = new MessageModel<List<SimpleAppModel>>();
		mm.setFlag(true);
		mm.setPage(pageInfo);
		mm.setData(list);
		return ok(Json.toJson(mm));
	}
    
	public static Result showImage(String filename) {

		String path = Play.application().path().getPath() + "/upload/"
				+ filename;

		try {
			response().setContentType("image");
			ByteArrayInputStream baos = new ByteArrayInputStream(
					IOUtils.toByteArray(new FileInputStream(new File(path))));
			return ok(baos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notFound(filename + " is Not Found!");
	}

    public static Result uploadAPK(Long cid) {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart apkfile = body.getFile("files[]");
        if (apkfile != null) {
            String path = Play.application().path().getPath() + "/upload/";

            String contentType = apkfile.getContentType();

            if (contentType == null || !contentType.startsWith("application/")) {
                return ok(Json.toJson("error:not apk file"));
            }

            File file = apkfile.getFile();
            // return redirect(routes.Courses.showImage(fileName));

        }
        return ok(Json.toJson("error:Missing file"));
    }
}

