package com.tehmou.fanexample;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ttuo on 28/07/16.
 */
public class AnimateToOperator {
    public static <T> Observable<T> animate(
            Observable<T> source,
            int durationMs,
            TypeEvaluator<T> typeEvaluator) {
        return Observable.create(new OnSubscribe<>(source, durationMs, typeEvaluator));
    }

    public static <T> Observable<T> animate(
            Observable<T> source,
            int durationMs) {
        return Observable.create(new OnSubscribe<>(source, durationMs, null));
    }

    private static class OnSubscribe<T> implements Observable.OnSubscribe<T> {
        final private TypeEvaluator<T> typeEvaluator;
        final private int durationMs;
        final private Observable<T> source;

        private ValueAnimator valueAnimator;
        private T lastValue;

        private OnSubscribe(
                Observable<T> source,
                int durationMs,
                TypeEvaluator<T> typeEvaluator) {
            this.source = source;
            this.durationMs = durationMs;
            this.typeEvaluator = typeEvaluator;
        }

        @Override
        public void call(final Subscriber<? super T> subscriber) {
            final Subscriber<T> nestedSubscriber = new Subscriber<T>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                }

                @Override
                public void onNext(T t) {
                    try {
                        startValueAnimator(lastValue, t);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }

                private void setValue(@Nullable T value) {
                    lastValue = value;
                    subscriber.onNext(value);
                }

                private void startValueAnimator(@Nullable T start, @Nullable T end) throws Exception {
                    // Reset any existing ValueAnimator.
                    if (valueAnimator != null) {
                        valueAnimator.end();
                        valueAnimator = null;
                    }

                    if (end == null) {
                        // Nothing to do here.
                        return;
                    } else if (start == null) {
                        // Start is not defined so just skip to the end.
                        setValue(end);
                        return;
                    }

                    // Start and end are non-null, proceed to animate.
                    if (start instanceof Integer) {
                        valueAnimator = ValueAnimator.ofInt((Integer) start, (Integer) end);
                    } else if (start instanceof Float) {
                        valueAnimator = ValueAnimator.ofFloat((Float) start, (Float) end);
                    } else if (typeEvaluator != null) {
                        valueAnimator = ValueAnimator.ofObject(typeEvaluator, start, end);
                    } else {
                        throw new Exception("Type is not Integer or Float but no typeEvaluator was specified.");
                    }
                    valueAnimator.setDuration(durationMs);
                    valueAnimator.addUpdateListener(animation -> {
                        if (subscriber.isUnsubscribed()) {
                            // The subscriber was unsubscribed, quit the animation.
                            valueAnimator.end();
                            unsubscribe();
                            return;
                        }
                        setValue((T) animation.getAnimatedValue());
                    });
                    valueAnimator.start();
                }
            };
            subscriber.add(source.subscribe(nestedSubscriber));
        }
    }
}
