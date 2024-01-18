/**
 * UserData class
 * Contains stats and information about a user
 */
export class UserData {
    id: string;
    username: string;
    email: string;
    watching: string;
    completed: string;
    on_hold: string;
    dropped: string;
    plan_to_watch: string;

    errorMessage: string;

    constructor(){
        this.errorMessage = '';
    }

    setValues(id: string, username: string, email: string, watching: string, completed: string, on_hold: string, dropped: string, plan_to_watch: string){
        this.id = id;
        this.username = username;
        this.email = email;
        this.watching = watching;
        this.completed = completed;
        this.on_hold = on_hold;
        this.dropped = dropped;
        this.plan_to_watch = plan_to_watch;
    }


    setErrorMessage(message: string){
        this.errorMessage = message;
    }

    getErrorMessage(): string{
        return this.errorMessage;
    }

}