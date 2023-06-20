import '../main.css';
import {useEffect, useState} from "react";
import axios from "axios";
import {TodoItemValue} from "../model/TodoItemValue";
import {REST_API_PORT} from "../App";

export default function Actions(props) {
    const [isEdited, setEdited] = useState(props.isEdited ?? false)
    const [previousValue, setPreviousValue] = useState(props.taskRef?.current?.value
        ?? props.taskValue);

    useEffect(() => {
        if(props.ifSave)
        {
            handleSave().catch();
            props.setIfSave(false);
        }
    }, [props.ifSave]);

    async function updateIsEdited(value) {
        const res = await axios.patch(`http://localhost:${REST_API_PORT}/api/todo-item/is-edited`,
            {id: props.taskId, edited: value}
        );
        if (res.status === 201) {

        } else {
            alert("Some problem occurred.")
        }
    }

    async function handleEditClick() {
        setPreviousValue(props.taskRef?.current?.value);
        props.setReadOnly(false);
        props.taskRef?.current?.focus();
        // props.taskRef.current.selectionStart = props.taskRef.current.value.length;
        // props.taskRef.current.selectionEnd = props.taskRef.current.value.length;

        setEdited(true);
        await updateIsEdited(true).catch();
    }

    async function deleteTask(taskId) {
        const res = await axios.delete(`http://localhost:${REST_API_PORT}/api/todo-item/${taskId}`)
        if (res.status !== 204)
        {
            alert("Some problem with removing the task occurred");
        }
        return res;
    }

    function handleDelete() {
        if (window.confirm(`Are you sure you want to delete the item which reads "${props.taskRef?.current?.value}"?`))
        {
            deleteTask(props.taskId)
                .then(() => { props.fetchData().catch(); console.log("Deleted") })
                .catch();
        }
    }

    async function handleSave() {
        if (props.taskRef?.current != null)
        {
            var currentTaskValue = props.taskRef?.current?.value;
            props.setTaskValue(currentTaskValue);
        }
        const todoItemValue = new TodoItemValue(props.taskId, currentTaskValue);
        console.log(todoItemValue);
        const res = await axios.patch(`http://localhost:${REST_API_PORT}/api/todo-item/value`, todoItemValue);
        if (res.status === 201) {

        } else {
            alert("Some problem with editing value occurred. Try again.")
        }
        setEdited(false);
        await updateIsEdited(false).catch()
        props.setReadOnly(true);
        props.taskRef?.current?.blur();
        // props.taskRef?.current?.setSelectionRange(0, 0);
    }

    async function handleUndo() {
        // props.taskRef?.current?.focus();
        props.setTaskValue(previousValue);
        setEdited(false);
        await updateIsEdited(false).catch()
        props.setReadOnly(true);
        props.taskRef?.current?.blur();
    }

    return <div className="actions">

        {/*<div className="spanActions">*/}
            {
            !isEdited ?
                <button className="edit" onClick={handleEditClick}>Edit</button>
                :
                <span className="spanActions"><button className="save" onClick={handleSave}>Save</button>
                <span>|</span>
                <button className="undo" onClick={handleUndo}>Undo</button></span>
            }
        <button className="delete" onClick={handleDelete}>Delete</button>
        {/*</div>*/}
    </div>;
}