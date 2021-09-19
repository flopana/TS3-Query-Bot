package Functions;

public interface IObservable {
    IObservable attach(IObserver observer);
    IObservable detach(IObserver observer);
    void update();
}
