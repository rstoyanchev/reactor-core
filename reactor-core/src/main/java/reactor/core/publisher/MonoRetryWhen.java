/*
 * Copyright (c) 2011-2017 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.core.publisher;

import java.util.Objects;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import reactor.core.CoreSubscriber;

/**
 * retries a source when a companion sequence signals an item in response to the main's
 * error signal
 * <p>
 * <p>If the companion sequence signals when the main source is active, the repeat attempt
 * is suppressed and any terminal signal will terminate the main source with the same
 * signal immediately.
 *
 * @param <T> the source value type
 * @see <a href="https://github.com/reactor/reactive-streams-commons">Reactive-Streams-Commons</a>
 */
final class MonoRetryWhen<T> extends MonoOperator<T, T> {

	final Function<? super Flux<Throwable>, ? extends Publisher<?>>
			whenSourceFactory;

	MonoRetryWhen(Mono<? extends T> source,
			Function<? super Flux<Throwable>, ? extends Publisher<?>> whenSourceFactory) {
		super(source);
		this.whenSourceFactory =
				Objects.requireNonNull(whenSourceFactory, "whenSourceFactory");
	}

	@Override
	public void subscribe(CoreSubscriber<? super T> actual) {
		FluxRetryWhen.subscribe(actual, whenSourceFactory, source);
	}

	@Override
	public Object scanUnsafe(Attr key) {
		if (key == Attr.RUN_STYLE) return Attr.RunStyle.SYNC;
		return super.scanUnsafe(key);
	}
}
