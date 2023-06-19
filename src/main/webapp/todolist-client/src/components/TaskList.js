import '../main.css';

import {useEffect} from "react";
import Task from "./Task";

export default function TaskList(props) {
    useEffect(() => {
        props.fetchData().catch();
    }, []);

    return (
        <main>
            <section className="task-list">
                <div id="tasks">
                    <div className="list-title"><h2>Tasks</h2></div>
                    {
                        props.todoTasks
                            ?.map(task =>
                                (<Task
                                    key={task?.todoItemId}
                                    task={task}
                                    fetchData={props.fetchData}
                                />))
                    }
                </div>
            </section>
        </main>);
}