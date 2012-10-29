import flask

from gansht_server.www import login
import gansht_server.models.models as models
import gansht_server.models.repository.repository as repository
import gansht_server.services.image_processor as img_processor


blueprint = flask.Blueprint("app_data", __name__)

@blueprint.route("/upload_data", methods=["POST"])
@login.with_user
def upload_data():
    test_area = flask.request.form["test_name"]
    b64_jpeg_image = flask.request.form["image"]
    user_id = flask.request.form["user_id"]
    test_id = flask.request.form["test_group"]
    request_id = flask.request.form["post_id"]

    repository.save_user_exam(
        models.UserExam(
            user_id=user_id,
            test_id=test_id,
            test_area=test_area,
            img_processor.process_answers(b64_jpeg_image)
        )
    )

    return request_id  # TODO add extra data
