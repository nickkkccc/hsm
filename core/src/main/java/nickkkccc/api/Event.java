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
 * Interface representing an event message processed by the Hierarchical State Machine (HSM).
 * <p>
 * Any object in the application can act as an HSM event trigger by implementing this interface.
 * This approach decouples the state machine infrastructure from the specific data models
 * of the host application, allowing users to pass raw signals, rich data containers,
 * network packets, or modern Java Records directly into the state engine.
 * </p>
 *
 * <h3>Event ID Conventions:</h3>
 * <ul>
 *   <li><b>Negative values ({@code < 0})</b> — Reserved strictly for built-in HSM infrastructure signals
 *       ({@link #START_EVT}, {@link #ENTRY_EVT}, {@link #EXIT_EVT}).</li>
 *   <li><b>Non-negative values ({@code >= 0})</b> — Available for application-defined, domain-specific events.</li>
 * </ul>
 *
 * <h3>Implementation Best Practices:</h3>
 * <ul>
 *   <li>Implementations should ensure that the integer returned by {@link #type()} remains constant
 *       for the entire lifecycle of the message object (immutability).</li>
 *   <li>For high-throughput systems, it is recommended to use modern Java {@code record} structures
 *       to carry data payloads alongside the event identifier.</li>
 * </ul>
 *
 * @author nickkkccc
 */
public interface Event {

    /**
     * Reserved signal value for state initialization and initial transitions.
     */
    int START_EVT = -1;

    /**
     * Reserved signal value executed immediately upon entering a new state configuration.
     */
    int ENTRY_EVT = -2;

    /**
     * Reserved signal value executed immediately before a state configuration is deactivated.
     */
    int EXIT_EVT = -3;

    /**
     * Pre-allocated infrastructure message sent to a state to trigger its default
     * substate activation or initial transition logic.
     */
    Event START = () -> START_EVT;

    /**
     * Pre-allocated infrastructure message sent to a state immediately after it becomes active.
     * Typically used to perform state-specific setups, allocations, or timers start.
     */
    Event ENTRY = () -> ENTRY_EVT;

    /**
     * Pre-allocated infrastructure message sent to a state right before it leaves the active configuration.
     * Typically used to perform cleanup, tear-downs, or resource deallocations.
     */
    Event EXIT = () -> EXIT_EVT;

    /**
     * Returns the unique integer identifier (signal type) of this event message.
     * <p>
     * The state machine engine relies exclusively on this integer value to evaluate guards,
     * trigger transitions, or bubble unhandled events up the state hierarchy tree.
     * </p>
     *
     * @return the integer event signal.
     */
    int type();


    /**
     * Evaluates whether this message's signal type matches the provided event type identifier.
     * <p>
     * This is a convenient helper method to write clean event dispatching logic inside
     * event handlers without manual extraction of the raw integer.
     * </p>
     * <p>
     * Usage example:
     * <pre>{@code
     * if (msg.is(Msg.ENTRY_EVT)) {
     *     // Execute custom state entry routine
     * }
     * }</pre>
     * </p>
     *
     * @param type the integer event type to compare against.
     * @return {@code true} if the signals match, {@code false} otherwise.
     */
    default boolean is(int type) {
        return this.type() == type;
    }
}
