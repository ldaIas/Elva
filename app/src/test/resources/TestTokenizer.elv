typedef surface <: (V: CounterModel, E: CounterMsg) 
  = { view: (V |-> ())
    , effects: List E
    }