db.createUser(
        {
            user: "theatre_admin",
            pwd: "theatre_admin",
            roles: [
                {
                    role: "readWrite",
                    db: "theatre"
                }
            ]
        }
);