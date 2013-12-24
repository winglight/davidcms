import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.avaje.ebean.Ebean;
import fetch.FetchWorker;
import models.CompanyModel;
import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import play.libs.Akka;
import play.libs.Yaml;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
			if (Ebean.find(CompanyModel.class).findRowCount() == 0) {

				Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
						.load("initial-data.yml");

				// Insert users first
				Ebean.save(all.get("company"));

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


