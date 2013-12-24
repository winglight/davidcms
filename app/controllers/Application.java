package controllers;

import static play.data.Form.form;

import models.UserModel;

import play.data.Form;
import play.mvc.*;

import util.Constants;
import views.html.*;

public class Application extends Controller {

    public static class Login {

        public String username;
        public String password;

        public String validate() {
            if (UserModel.authenticate(username, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    public static Result index() {
        return ok(intro.render(null));
    }

    public static Result login() {
        return ok(login.render(form(Login.class)));
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.login());
    }

    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session(Constants.SESSION_USER_NAME, loginForm.get().username);
            return redirect(routes.Application.index());
        }
    }

    public static Result register() {
            return redirect(routes.Application.index());

    }
}
