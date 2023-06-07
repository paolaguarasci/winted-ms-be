#!/bin/bash
set -eu
mongosh -u $MONGO_INITDB_ROOT_USERNAME -p $MONGO_INITDB_ROOT_PASSWORD <<EOF
    db = db.getSiblingDB('winted-product-service');
    use winted-product-service;
    db.createUser({
        user: 'winted-product-service',
        pwd: 'winted-product-service',
        roles: [{
            role: 'readWrite',
            db: 'winted-product-service'
        }]
    });

    db = db.getSiblingDB('winted-message-service');
    use winted-message-service;
    db.createUser({
        user: 'winted-message-service',
        pwd: 'winted-message-service',
        roles: [{
            role: 'readWrite',
            db: 'winted-message-service'
        }]
    });

    db = db.getSiblingDB('winted-profile-service');
    use winted-profile-service;
    db.createUser({
        user: 'winted-profile-service',
        pwd: 'winted-profile-service',
        roles: [{
            role: 'readWrite',
            db: 'winted-profile-service'
        }]
    });
    
    show dbs;
EOF
