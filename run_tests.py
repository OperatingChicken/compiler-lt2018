#!/usr/bin/env python3

import json
import subprocess
import platform
import tempfile

if __name__ == "__main__":
    if platform.system() == "Windows":
        subprocess.run(["cmd.exe", "gradlew.bat", "jar"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    else:
        subprocess.run(["./gradlew", "jar"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    config = json.loads(open("tests/config.json", "r").read())
    for test in config:
        print(test["name"], ": ", end='', sep='')
        with tempfile.TemporaryDirectory() as tempdir:
            status = subprocess.run(["java", "-jar", "build/libs/compiler-lt2018.jar", "tests/" + test["source"], "-o", tempdir + "/output.bin"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            if (test["compileShouldFail"] and status.returncode == 0) or (not test["compileShouldFail"] and status.returncode != 0):
                print("FAIL (compile error)")
                continue
            if test["compileShouldFail"]:
                print("OK")
                continue
            status = subprocess.run([tempdir + "/output.bin"], input=test["input"].encode(), stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            if status.returncode != 0:
                print("FAIL (non-zero return code)")
            if test["output"] == status.stdout.decode():
                print("OK")
            else:
                print("FAIL (different output)")
