import elva.std.iofx using Debug, SimpleShellOut as Out, SimpleShellIn as In

/**
 * The purpose is a special `record` that tells us what the program _is_.
 * This acts as the main function. The Elva runtime looks for the purpose definition and invokes the surface
 *          view with the model record returned by the model function. It will then loop according to the surface's events.
 * It tells us what the internal state will be (model) and how that will be presented to users (surface) as well as how to handle signals.
 * The purpose type has special definitions:
 * - `model` must be a function that returns a record of the V type. In this case it returns an initialized CounterModel
 * - `surface` must be a function that returns a special declared `surface` definition
 *          that is constrained by the V type for the input model and the E type for the effects produced
 * - `update` must be a function that conforms to the updateInterface: takes in a `record` and `msg` and produces a record in the same shape as the input
                Must take in the same E type as the surface
 */
purpose CounterAppPurpose <: V: CounterModel, E: CounterMsg =
    { model = \_ -> CounterModel {}
    , surface = \_ -> CounterSurface
    , update = update
    }

/**
 * msg is a special type that denotes the message signals the app will need to handle. Very similar to Elm's `type`
 * This msg tells whether we should increment or decrement the current counter, or if the last input was InvalidInput
 */
msg CounterMsg
    = Increment
    | Decrement
    | InvalidInput

/**
 * Basic model that is created by a record. This is similar to `type alias` in Elm
 * records have overrideable methods (denoted by +) that are otherwise provided by the language by default
 * This record keeps track of the current count in the counter app model
 */
record CounterModel =
    { count : Int
    
    + init = CounterModel 0 // Could also do CounterModel(0) or {count = 0}
    // TODO Allow overriding any non-forbidden string of characters for methods
    // IE "+ add (self: CounterModel) |-> CounterModel = { self | count = current + 1}"
    // Usage: "fn updateModel(model: CounterModel) |-> CounterModel = model.add
    // Multi param: "+ mergeCounts(self: CounterModel, other: CounterModel) |-> CounterModel = { self | count = current + other.count}
    // Usage: "fn addModels(m1: CounterModel, m2: CounterModel) |-> CounterModel = m1.mergeCounts m2
    }

// This should be in the standard library that gets auto imported
// typedef surface <: (V: record, E: msg) = 
//     { view: (V |-> ())
//     , events: List E
//     }

/**
 * A surface is a way of presenting or "feeling" the model. It is typed by the Purpose and Message types.
 * The update function must use the same Message type the surface does
 * Basic surface for Counter purposes that displays the current count to the shell, and asks for user input via stdin
 */
 // optionally:
 // surface CounterSurface <: (V: CounterModel, E: CounterMsg) =
surface CounterSurface <: V: CounterModel, E: CounterMsg =
    {
    draw = \counterModel -> SimpleShellOut ("Current count is" ++ counterModel.count) // SimpleShellout is standard library renderer. eq to {outStr = "..."}
                                           (SimpleShellIn ("Enter command: increment(i, +) or decrement(d, -)")
                                            (\userInput -> case String.lower(userInput) of: 
                                                "+", "i" -> Increment
                                                "-", "d" -> Decrement
                                                _ -> InvalidInput)
                                           )
    }

/**
 * Function to update the model. Takes in a model and outputs a new model. Typed by the Message
 * Note the parans () are not necessary. If there was only one input it'd look like `fn update inModel: Model |-> Model`
 * This function updates the model by adding or subtracting based on the input.
 */
fn update (inModel: CounterModel, inMsg: CounterMsg) |-> (CounterModel, List Effect) =
    case inMsg of:
        Increment -> ( { inModel | count = current + 1 } // This line is shorthand to create a new CounterModel using the same values as inModel
                                                         // while updating the "count" field to be the current "count" field's value + 1.
                                                         // "current" is shorthand for referencing the field on the LHS of assignment in the input record.
                                                         // would be like `CurrentModel (inModel.count + 1)` or `inModel | count = inModel.count + 1`
                      , [ Debug "Increment pressed. Current model is " ++ inModel ] )
        Decrement -> ({ inModel | count = current - 1 }, Debug "Decrement pressed. Current model is " ++ inModel)
        InvalidInput -> (inModel, [ Debug "Invalid input entered, nothing done" ]) 