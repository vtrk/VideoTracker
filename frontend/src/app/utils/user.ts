export class User {
    id_user: string;
    email: string;
    username: string;
    password: string;
    completed: string;
    watching: string;
    onHold: string;
    planned: string;
    dropped: string;

    constructor() {}

    setValues(id_user: string, email: string, username: string, password: string, completed: string, watching: string, onHold: string, planned: string, dropped: string) {
        this.id_user = id_user;
        this.email = email;
        this.username = username;
        this.password = password;
        this.completed = completed;
        this.watching = watching;
        this.onHold = onHold;
        this.planned = planned;
        this.dropped = dropped;
    }
}