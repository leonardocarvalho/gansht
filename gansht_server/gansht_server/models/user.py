from cryptacular import bcrypt


class User(object):

    _pwd_manager = bcrypt.BCRYPTPasswordManager()

    def __init__(self, username, password=None, _id=None):
        self._id = _id
        self.username = username
        if password:
            self.set_password(password)

    def set_password(self, password):
        self._password = self._pwd_manager.encode(password)

    def has_password(self, password):
        return self._pwd_manager.check(self._password, password)

    def serialize(self):
        return {
            "username":  self.username,
            "password": self._password,
        }

    @classmethod
    def load(cls, data):
        user = cls(data["username"], _id=data.get("_id"))
        user._password = data["password"]
        return user


class UserExam(object):

    def __init__(self, test_id=None, test_area=None, user_id=None, answers=None):
        self.test_id = test_id
        self.test_area = test_area
        self.user_id = user_id
        self.answers = answers or []
        self.__generate_id()

    @property
    def _id(self):
        return self.generate_id(self.user_id, self.test_id, self.test_area)

    @staticmethod
    def generate_id(user_id, test_id, test_area):
        return user_id + test_id + test_area

    def __generate_id(self):
        self._id = UserExam.generate_id(self.user_id, self.test_id, self.test_area)

    def __setattr__(self, key, value):
        super(UserExam, self).__setattr__(key, value)
        self.__generate_id()

    def serialize(self):
        return {
            "_id": self._id,
            "test_id": self.test_id,
            "test_area": self.test_area,
            "user_id": self.user_id,
            "answers": self.answers,
        }

    @classmethod
    def load(cls, data):
        self._id = data.get("_id")
        self.test_id = data.get("test_id")
        self.test_area = data.get("test_area")
        self.user_id = data.get("user_id")
        self.answers = data.get("answers")
