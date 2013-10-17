import flask

from gansht_server.www import login


blueprint = flask.Blueprint("app_data", __name__)

@blueprint.route("/upload_data", methods=["POST"])
@login.with_user
def upload_data(user):
    print flask.request.form
    return flask.request.form.get("post_id")
