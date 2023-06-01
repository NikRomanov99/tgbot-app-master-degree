db = db.getSiblingDB('reports');
db.createUser(
    {
        user: "nikitadev",
        pwd: "mongopassword",
        roles: [{role: "readWrite", db: "reports"}, {role: "readWrite", db: "userData"}]
    },
);

