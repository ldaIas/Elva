typedef surface <: (V: CounterModel, E: CounterMsg) 
  = { view: ((V, E) |-> ())
    , effects: List E
    }