import flask
import functools
import random
import string

from gansht_server.models import repository


blueprint = flask.Blueprint("login", __name__)


def with_user(decorated):

    @functools.wraps(decorated)
    def decorator(*args, **kwargs):
        cont = flask.request.form if flask.request.method.lower() == "post" else flask.request.args
        user = repository.user.get_user_by_auth(cont["auth"])
        return decorated(user, *args, **kwargs)

    return decorator


@blueprint.route("/login", methods=["POST"])
def login():
    username = flask.request.form["username"]
    password = flask.request.form["password"]

    user = repository.user.find_by_username(username)
    if user is None or not user.has_password(password):
        raise Exception("User mismatch")

    auth = generate_random_string(15)
    repository.user.save_authorization(auth, user._id)
    return auth


def generate_random_string(size=10, charset=string.letters + string.digits):
    return "".join(random.choice(charset) for _ in xrange(size))


# TODO: remove this code

@blueprint.route("/find_user", methods=["GET"])
def find_user():
    username = flask.request.args["username"]
    password = flask.request.args["password"]
    user = repository.user.find_by_username(username)
    if user.has_password(password):
        return "ok"
    return "fail"


@blueprint.route("/create_user", methods=["GET"])
def create_user():
    username = flask.request.args["username"]
    password = flask.request.args["password"]
    from gansht_server import scripts
    scripts.add_user(username, password)
    return "ok"
