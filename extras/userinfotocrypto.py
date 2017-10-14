#!/usr/bin/env python

import sys
from Crypto.Hash import SHA512
from Crypto.Hash import MD5


if len(sys.argv) != 3:
    print('Usage: userinfotocrypto.py [username] [password]')
    exit()

def checkuser(user):
    if '@' in user and not ' ' in user:
        return True
    else:
        return False

def hashuser(user):
    if checkuser(user):
        hasher = MD5.new()
        hasher.update(user.encode('utf-8'))
        return hasher.hexdigest()
        
    else:
        print('User must be an email address')
        exit()

def salt(user):
    hasher = MD5.new()
    hasher.update(user.encode('utf-8'))
    salted = ''
    for i in range(len(hasher.hexdigest())):
        salted = salted + chr(ord(hasher.hexdigest()[i]) + 4)
    return salted

def checkpass(passwd):
    if len(passwd) > 7:
        return True
    else:
        return False

def hashpass(passwd, salt=None):
    if checkpass(passwd):
        hasher = SHA512.new()
        password = passwd
        if salt:
            password = passwd + salt
        hasher.update(password.encode('utf-8'))
        return hasher.hexdigest()
    else:
        print('Password must be longer than 7 characters')
        exit()

print('Username:', sys.argv[1])
print('Password:', sys.argv[2])
username = salt(sys.argv[1])
print('Salted user:', username)
password = hashpass(sys.argv[2], username)
print('User hash with MD5:', hashuser(sys.argv[1]))
print('Password hash with SHA512:', hashpass(sys.argv[2]))
print('Password hash + salt:', password)
