class Field(object):

    def __init__(self, value=None):
        self.__value = value

    @property
    def value(self):
        return self.__value

    @value.setter
    def value(self, val):
        self.__value = val


class Entity(object):

    def __init__(self, **fields):
        for key, value in fields.items():
            if hasattr(self.__class__, key):
                setattr(self, key, value)
            else:
                raise ValueError("Class {} has no attribute {}".format(key, value))

    def __getattribute__(self, name):
        field = super(Entity, self).__getattribute__(name)
        if isinstance(field, Field):
            return field.value
        return field

    def __setattr__(self, name, value):
        field = super(Entity, self).__getattribute__(name)
        if isinstance(field, Field):
            field.value = value
        else:
            super(Entity, self).__setattr__(name, value)

    def serialize(self):
        serialized = {
            key: field.value
            for key, field in self.__class__.__dict__.items()
            if isinstance(field, Field)
        }


class UserExam(Entity):
    _id = Field()
    test_area = Field()
    user_id = Field()
    test_id = Field()
    answers = Field()

    @staticmethod
    def generate_id(user_id, test_id, test_area):
        return user_id + test_id + test_area

    def __init__(self, user_id, test_id, test_area):
        self.user_id = user_id
        self.test_id = test_id
        self.test_area = test_area
        self.__generate_id()

    def __generate_id(self):
        self._id = UserExam.generate_id(self.user_id, self.test_id, self.test_area)

    def __setattr__(self, key, value):
        super(UserExam, self).__setattr__(key, value)
        self.__generate_id()


class Admin(Entity):
    pass
