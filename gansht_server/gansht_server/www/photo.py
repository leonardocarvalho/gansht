import flask


blueprint = flask.Blueprint("app_data", __name__)

@blueprint.route("/upload_data", methods=["POST"])
def upload_data():

    print flask.request.form

    return flask.request.form.get("post_id")
