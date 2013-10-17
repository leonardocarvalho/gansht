import sys
import setuptools


setuptools.setup(
    name="gansht",
    version="1.0",
    url="http://gansht.herokuapp.com",
    maintainer="Geekie",
    maintainer_email="leonardo@geekie.com.br",
    packages=["gansht_server"],
    include_package_data=True,
    zip_safe=False,
    install_requires = [
        "flask>=0.8",
        "flask-pymongo>=0.1"
    ]
)
