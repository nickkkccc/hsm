/*
 * Copyright (C) 2026 Nickolay Belonogov (nickkkccc)
 *
 * Ported from the original work by Miro Samek (Quantum Leaps (miro@quantum-leaps.com))
 * under the Apache License, Version 2.0.
 * Derived work includes custom enhancements and modifications by ${project.organization.name}.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://apache.org
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nickkkccc.api;

/**
 * Функциональный интерфейс для обработки событий в иерархической машине состояний (HSM).
 * <p>
 * Реализации этого интерфейса инкапсулируют логику реакции на конкретные события
 * в зависимости от текущего контекста выполнения машины состояний.
 * </p>
 *
 * @author Nikolay Belonogov (nickkkccc)
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Обрабатывает поступившее событие в заданном контексте машины состояний.
     *
     * @param ctx   контекст иерархической машины состояний (инстанс {@link IHsm}),
     *              предоставляющий доступ к текущему состоянию и методам управления
     * @param event входящее событие (инстанс {@link Event}), которое необходимо обработать
     * @return {@link Event} результат обработки события (например, событие-подтверждение,
     * новое транзитное событие или {@code null}, если дальнейшая обработка не требуется)
     * @author Nikolay Belonogov (nickkkccc)
     */
    Event handle(IHsm ctx, Event event);
}
