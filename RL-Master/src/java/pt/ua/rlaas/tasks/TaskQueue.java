package pt.ua.rlaas.tasks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.rlaas.RecordUpdate;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TaskQueue {

    public enum Status {

        NEW, RUNNING, COMPLETE
    }

    public enum Type {

        TRANSFORM, COMPARE, EXPORT, UPDATE, PIPELINE, STOP_PIPELINE
    }

    public class QueueElement {

        public Type type;
//        public List<QueueElement> dependencies;
        public Object task;
        public int id;
        public Status status = Status.NEW;

        public QueueElement(Type type, Object task, int id) {
            this.type = type;
            this.task = task;
            this.id = id;
        }

        @Override
        public String toString() {
            return "QueueElement{" + "type=" + type + ", id=" + id + ", status=" + status + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final QueueElement other = (QueueElement) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }
    }
    private LinkedList<QueueElement> tasks = new LinkedList<QueueElement>();
    private HashMap<Integer, QueueElement> taskMap = new HashMap<Integer, QueueElement>();
    private static TaskQueue instance;
    private int nextId = 0;

    public synchronized static TaskQueue instance() {
        if (instance == null) {
            instance = new TaskQueue();
        }
        return instance;
    }

    private TaskQueue() {
    }

//    public synchronized void add(TransformTask transform) {
//        QueueElement q = new QueueElement(Type.TRANSFORM, transform, nextId);
//
//        tasks.add(q);
//        taskMap.put(nextId, q);
//
//        nextId++;
//
//        System.out.println("ADDED: List" + tasks.toString());
//        notifyAll();
//    }
    public synchronized void add(UpdatePipeline pipeline) {
        QueueElement q = new QueueElement(Type.PIPELINE, pipeline, nextId);

        tasks.add(q);
        taskMap.put(nextId, q);

        nextId++;

        notifyAll();
    }

    public synchronized void add(RecordUpdate update) {
        QueueElement q = new QueueElement(Type.UPDATE, update, nextId);

        tasks.add(q);
        taskMap.put(nextId, q);

        nextId++;

        notifyAll();
    }

    public synchronized void add(String pipelineToStop) {
        QueueElement q = new QueueElement(Type.STOP_PIPELINE, pipelineToStop, nextId);

        tasks.add(q);
        taskMap.put(nextId, q);

        nextId++;

        notifyAll();
    }

    public synchronized boolean ready(int id) {
        QueueElement e = taskMap.get(new Integer(id));

        return e.equals(tasks.getFirst()) && e.status == Status.NEW;
    }

    public synchronized int getSize() {
        return tasks.size();
    }

    public synchronized void markComplete(int id) {
        QueueElement e = taskMap.get(new Integer(id));

        if (e == null || e.status == Status.COMPLETE) {
            //ERROR
        } else {
            e.status = Status.COMPLETE;
        }

        tasks.remove(e);


        notifyAll();
    }

    public synchronized QueueElement getAvailableTask() {

        while (tasks.isEmpty() || !ready(tasks.getFirst().id)) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }

        QueueElement t = tasks.getFirst();
        t.status = Status.RUNNING;

        return t;
    }
}
