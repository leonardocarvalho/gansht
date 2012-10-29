import flask


blueprint = flask.Blueprint("results", __name__)

@blueprint.route("/")
def home():
    return flask.redirect(flask.url_for("results.search_tests"))

@blueprint.route("/search_tests")
def search_tests():
    return "Search results"

@blueprint.route("/view_test")
def view_result():
    return "View test"

