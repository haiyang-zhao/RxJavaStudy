package com.zhy.rxjavastudy;

import org.junit.Test;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RxJavaTest {
    @Test
    public void test() {

        ObservableOnSubscribe<Integer> observableOnSubscribe = new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                System.out.println("ObservableOnSubscribe");
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        };
        Observable<Integer> observable = Observable.create(observableOnSubscribe);
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("observer : onSubscribe");

            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("observer : onNext " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("observer : onError");
            }

            @Override
            public void onComplete() {
                System.out.println("observer : onComplete");
            }
        };

        observable.map(String::valueOf).subscribe(observer);
    }


    @Test
    public void test_thread_switch() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            System.out.println("ObservableOnSubscribe " + Thread.currentThread().getName());
            emitter.onNext(1);
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(integer -> System.out.println("Consumer " + Thread.currentThread().getName()),
                        Throwable::printStackTrace);

    }

    @Test
    public void test_thread_switch_multiple() {
        Observable
                .just(0)
                .observeOn(Schedulers.computation())
                .map(i -> i + 1)
//                .subscribeOn(Schedulers.newThread())
//                .subscribeOn(Schedulers.io()) // 多了这一行
                .subscribe(System.out::println);
    }
}