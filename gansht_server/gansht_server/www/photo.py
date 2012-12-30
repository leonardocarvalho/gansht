from bson import objectid
import flask
import json

from gansht_server import models
from gansht_server.models import repository
from gansht_server.services import image_processor
from gansht_server.www import login


blueprint = flask.Blueprint("app_data", __name__)


@blueprint.route("/upload_data", methods=["POST"])
@login.with_user
def upload_data(user):
    test_id = flask.request.form["test_id"]
    test_name = flask.request.form["test_name"]
    student_id = flask.request.form["user_id"]
    request_id = flask.request.form["post_id"]
    b64_jpeg_image = flask.request.form["image"]

    repository.image_dump.save({
        "test_id": test_id,
        "test_name": test_name,
        "student_id": student_id,
        "b64_jpeg_image": b64_jpeg_image,
    })

    answers = image_processor.process_answers(b64_jpeg_image)
#    repository.save_user_exam(
#        models.UserExam(
#            user_id=student_id,
#            test_id=test_id,
#            test_area=test_name,
#            answers=answers,
#        )
#    )

    return request_id  # TODO add extra data


@blueprint.route("/print-exam")
def print_exam():
    user_id = objectid.ObjectId(flask.request.args["user_id"])
    tests_id = flask.request.args["test_id"]
    return json.dumps(
        [x.serialize() for x in repository.user_exam.get_exams(user_id, test_id)],
        indent=4
    )


@blueprint.route("/upload_test", methods=["GET"])
def upload_test():
    from gansht_server import mongo
    student_id = flask.request.args["student_id"]
    test_id = flask.request.args["test_id"]
    data = list(mongo.db.image_dump.find({"student_id": student_id, "test_id": test_id}))
    if not data:
        return "none"
    a = [image_processor.process_answers(x["b64_jpeg_image"]) for x in data]
    return json.dumps(a, indent=4)
