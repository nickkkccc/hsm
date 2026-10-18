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
 * Core interface representing a Hierarchical State Machine (HSM).
 * <p>
 * Unlike traditional Finite State Machines (FSMs), an HSM organizes its states hierarchically
 * in a parent-child tree structure. This allows for behavioral inheritance: if a nested
 * child state does not handle an incoming event, the event automatically bubbles up
 * to its parent state for evaluation.
 * </p>
 *
 * <h3>State Machine Architecture & Lifecycle:</h3>
 * <ul>
 *   <li><b>Initialization:</b> The lifecycle begins by invoking {@link #onStart()}, which
 *       activates the default top-level state and drills down to the initial active leaf state.</li>
 *   <li><b>Event Bubbling:</b> Events dispatched via {@link #onEvent(Event)} are first offered
 *       to the currently active leaf state. If unhandled (the handler returns the event instead of
 *       {@code null}), the event ascends the {@code superState} chain until consumed or the root is reached.</li>
 *   <li><b>Transitions:</b> State transitions trigger a sequence of deterministic actions:
 *       calculating the Lowest Common Ancestor (LCA), unwinding the current branch via {@code Event.EXIT}
 *       signals, and drilling down into the target branch via {@code Event.ENTRY} and {@code Event.START} signals.</li>
 * </ul>
 *
 * @author nickkkccc
 */
public interface IHsm {

    /**
     * The maximum allowed nesting depth for hierarchical states.
     * <p>
     * This limit restricts how deep the state tree can be and serves as a strict boundary
     * for state execution loops (such as calculating transition topologies or buffering entry tracks).
     * Exceeding this boundary in deep hierarchies or complex nested initialization chains
     * may lead to array boundary overflows.
     * </p>
     */
    int MAX_STATE_NEXTING = 8;

    /**
     * Starts the hierarchical state machine execution engine.
     * <p>
     * This method resets the engine, sets the initial configuration to the implicit root state,
     * and sequentially executes initial transitions down to the deepest nested default state.
     * During this process, appropriate {@code Event.ENTRY} and {@code Event.START} signals
     * are dispatched to the participating states.
     * </p>
     * <p>
     * <b>Thread Safety Notice:</b> This method modifies the internal topology state variables.
     * It must not be called concurrently while the machine is actively dispatching events.
     * </p>
     *
     * @throws IllegalStateException if the machine is already initialized or in an invalid setup state.
     */
    void onStart();


    /**
     * Dispatches an event message to the state machine for processing.
     * <p>
     * The event processing loop targets the most specific active state first. The event
     * handler intercepts the event:
     * <ul>
     *   <li>If the state consumes the event, it returns {@code null}, terminating the processing path.</li>
     *   <li>If the state does not recognize the event, it returns the event object, causing the
     *       loop to bubble the event up to the {@code superState}.</li>
     * </ul>
     * </p>
     * <p>
     * If an event handling routine triggers a state transition (via a transition descriptor),
     * the method will intercept the target specification, unwind the deactivated states up
     * to the common ancestor, and perform a structural downward entry chain into the new state branch.
     * </p>
     *
     * @param event the {@link Event} signal descriptor to be processed by the active state hierarchy.
     * @throws NullPointerException  if the passed event parameter is {@code null}.
     * @throws IllegalStateException if a double transition violation occurs within a single step.
     */
    void onEvent(Event event);
}
