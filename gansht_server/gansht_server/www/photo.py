import flask


blueprint = flask.Blueprint("app_data", __name__)

@blueprint.route("/upload_data")
def upload_data():
    pass
