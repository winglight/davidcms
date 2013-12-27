import akka.actor.ActorRef;
import com.avaje.ebean.Ebean;
import models.CompanyModel;
import models.UserModel;
import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {

	public static final String MSG_REFETCH_POST = "fetch_posts";

	ActorRef tickActor;

	public <T extends EssentialFilter> Class<T>[] filters() {
		return new Class[] { GzipFilter.class };
	}

	public void onStart(Application app) {
		InitialData.insert(app);

	}

	static class InitialData {

		public static void insert(Application app) {
			if (Ebean.find(UserModel.class).findRowCount() == 0) {

				Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
						.load("initial-data.yml");

				// Insert users first
				Ebean.save(all.get("company"));

                Ebean.save(all.get("users"));

				// Insert projects
//				Ebean.save(all.get("sources"));
				// for(Object source: all.get("sources")) {
				// // Insert the source/user relation
				// Ebean.saveManyToManyAssociations(source, "createdBy");
				// }

				// Insert tasks
				// Ebean.save(all.get("tasks"));

			}
		}

	}
}


