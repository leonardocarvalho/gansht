import gansht_server.models.models as models
import gansht_server.mongo as mongo


def save_user_exam(user_exam):
    mongo.db.user_exams.save(user_exam)


def get_exams_for_test_id(test_id):
    return [
        models.UserExam(**serialized)
        for serialized in mongo.db.user_exams.find({"test_id": test_id})
    ]
