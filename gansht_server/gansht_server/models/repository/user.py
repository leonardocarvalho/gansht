from gansht_server import mongo
from gansht_server.models import user


class UserRepository(object):

    model = user.User

    def find_one(self, _id):
        record = mongo.db.users.find_one({"_id": _id})
        if record is None:
            return None
        return self.model.load(record)

    def find_by_username(self, username):
        record = mongo.db.users.find_one({"username": username})
        if record is None:
            return None
        return self.model.load(record)

    def save_user(self, user):
        mongo.db.users.update({"username": user.username},
                              {"$set": user.serialize()},
                              upsert=True)

    def save_authorization(self, auth, user_id):
        mongo.db.authorizations.insert({"_id": auth, "user_id": user_id})

    def get_user_by_auth(self, auth):
        auth = mongo.db.authorizations.find_one({"_id": auth})
        if auth is None:
            return None
        return self.find_one(auth["user_id"])


it = UserRepository()
