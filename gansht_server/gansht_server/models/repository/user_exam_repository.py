from gansht_server import mongo
from gansht_server import models


class UserExamRepository(object):

    def save(self, user_exam):
        mongo.db.exams.update({"_id": user_exam._id}, {"$set": user_exam.serialize()}, upsert=True)

    def get_exams(self, user_id=None, test_id=None):
        query = {key: value for key, value in (("user_id", user_id), ("test_id", test_id)) if value}
        return map(models.UserExam.load, mongo.exams.find(query))


it = UserExamRepository()
