package com.example.administrator.myapplication.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by cai.jia on 2017/4/14 0014
 */

public class RxDemo {

    public static void main(String[] args) {

        RxDemo rx = new RxDemo();
        rx.fun7();

    }

    private void fun7() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("exception:" + (1 / 1));
                e.onNext("exception:" + (1 / 0));
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

    }

    private void fun6() {
        Flowable.fromArray(1, 2, 3, 4, 5)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("will print " + integer);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });

    }

    private void fun5() {
        Flowable.fromArray(1, 2, 3, 4, 5)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    private void fun4() {
        Flowable.fromArray(1, 2, 3, 4, 5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer ints) throws Exception {
                        return ints > 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }

    private void fun3() {
        List<String> list = new ArrayList<>();
        list.add("android");
        list.add("ios");
        Flowable.just(list)
                .flatMap(new Function<List<String>, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull List<String> list) throws Exception {
                        return Flowable.fromIterable(list);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    private void fun2() {
        List<String> list = new ArrayList<>();
        list.add("android");
        list.add("ios");
        Flowable.fromIterable(list)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }

    private void fun1() {
        Observable.just("android", "ios")
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return Observable.just(s + "flatMap");
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return Observable.just(s + "flatMap2");
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println(s);
                    }
                });

    }
}
