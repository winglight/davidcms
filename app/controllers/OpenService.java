package controllers;

import models.CompanyModel;
import models.ContentModel;
import models.dto.MessageModel;
import models.status.ContentType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.Constants;

import java.io.*;
import java.util.List;

import static play.data.Form.form;

public class OpenService extends Controller {

    public static Result getCompany() {
        CompanyModel cm = CompanyModel.find.findUnique();
    	
		MessageModel<CompanyModel> mm = new MessageModel<CompanyModel>();
		mm.setFlag(true);
		mm.setData(cm);
		return ok(Json.toJson(mm));
	}
    
    public static Result getContentsByType(Long category) {
    	List<ContentModel> list = ContentModel.getContentsByType(category);

		MessageModel<List<ContentModel>> mm = new MessageModel<List<ContentModel>>();
		mm.setFlag(true);
		mm.setData(list);
		return ok(Json.toJson(mm));
	}

    public static Result getContentByID(Long cid) {
    	ContentModel cm = ContentModel.find.byId(cid);

		MessageModel<ContentModel> mm = new MessageModel<ContentModel>();
		mm.setFlag(true);
		mm.setData(cm);
		return ok(Json.toJson(mm));
	}

	public static Result showImage(String filename) {

		String path = Play.application().path().getPath() + "/upload/"
				+ filename;

		try {
			response().setContentType("image");
			ByteArrayInputStream bais = new ByteArrayInputStream(
					IOUtils.toByteArray(new FileInputStream(new File(path))));
			return ok(bais);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notFound(filename + " is Not Found!");
	}

}

