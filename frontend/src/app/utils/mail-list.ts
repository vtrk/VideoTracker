/**
 * Representation of a mail (notification)
 */
class Mail {
    id: string;
    description: string;
    title: string;
}

/**
 * Representation of a list of mails (list of notifications)
 */
export class ItemMailList {
    list: Mail[];

    error_string: string = "";

    constructor(){
        this.list = [];
    }

    add(id: string, description: string, title: string){
        var item = new Mail();
        item.id = id;
        item.description = description;
        item.title = title;
        this.list.push(item);
    }

    getMailList(){
        return this.list;
    }

    setErrorString(error_string: string){
        this.error_string = error_string;
    }

    getErrorString(){
        return this.error_string;
    }
}