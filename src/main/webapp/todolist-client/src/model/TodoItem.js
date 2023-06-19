export class TodoItem {

    constructor(itemValue, createdOn) {
        this.itemValue = itemValue;
        this.createdOn = createdOn;
    }

    todoItemId = null;
    itemValue;
    createdOn;
    lastEditOn = null;
    deadline = null;
    done = false;
    checked = false;
    edited = false;
    wasEdited = false;
}