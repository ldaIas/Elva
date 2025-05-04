fn update (inModel: CounterModel, inMsg: CounterMsg) |-> (CounterModel, List, Effect) = 
    match inMsg:
        Inc -> ()
        Dec -> ()