import '../main.css';
import axios from "axios";
import {TodoItem} from "../model/TodoItem";
import {useState, useEffect} from "react";
import moment from "moment";
import {REST_API_PORT} from "../App";

export default function Header(props)
{
    const [fetchFlag, setFetchFlag] = useState(false);
    useEffect(() => {
        props.fetchData().catch();
    }, [fetchFlag])

    async function handleSubmit(e) {
        e.preventDefault();
        const taskValue = e.target.elements["new-task-input"].value;
        console.log(new Date().toLocaleString('en-GB' ))
        // { timeZone: 'UTC' }
        // YYYY-MM-DDTHH:mm:ss.sssZ
        const todoItem = new TodoItem(taskValue, moment().toISOString(true));
        console.log(todoItem);
        try {
            const res = await axios.post(`http://localhost:${REST_API_PORT}/api/todo-item`, todoItem);
            if (res.status === 201) {
                e.target.elements["new-task-input"].value = "";
                setFetchFlag(prev => !prev);
            } else {
                alert("Some problem with adding new task occurred. Try again.")
            }
        }
        catch (exc)
        {
            alert("Some problem with adding new task occurred. Try again.")
            console.log(exc.message);
        }
    }

    return (<header>

            <h1>TaskApp 2023</h1>
             <section className="add-task">
                <form id="new-task-form" onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="new-task-input"
                        id="new-task-input"
                        placeholder="What are you going to do?" />
                    <input
                        type="submit"
                        id="new-task-submit"
                        value="Add task" />
                </form>
            </section>
    </header>);
}