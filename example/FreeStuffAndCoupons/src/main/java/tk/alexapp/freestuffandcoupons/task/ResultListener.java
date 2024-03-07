package tk.alexapp.freestuffandcoupons.task;

public interface ResultListener<T> {

    void success(T result);

    void error();
}
