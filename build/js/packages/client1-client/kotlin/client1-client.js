(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'client1-client'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'client1-client'.");
    }root['client1-client'] = factory(typeof this['client1-client'] === 'undefined' ? {} : this['client1-client'], kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var Unit = Kotlin.kotlin.Unit;
  var throwCCE = Kotlin.throwCCE;
  function main$lambda$lambda(it) {
    println('connected');
    return Unit;
  }
  function main$lambda$lambda_0(it) {
    var tmp$, tmp$_0;
    var div = Kotlin.isType(tmp$ = document.getElementById('history'), HTMLDivElement) ? tmp$ : throwCCE();
    div.innerHTML = (typeof (tmp$_0 = it.data) === 'string' ? tmp$_0 : throwCCE()) + '<br>' + div.innerHTML;
    return Unit;
  }
  function main$lambda$lambda_1(closure$ws) {
    return function (it) {
      var tmp$;
      var message = (Kotlin.isType(tmp$ = document.getElementById('message'), HTMLInputElement) ? tmp$ : throwCCE()).value;
      closure$ws.send(message);
      return Unit;
    };
  }
  function main() {
    var tmp$;
    var $receiver = new WebSocket('ws://localhost:8080/site');
    $receiver.onopen = main$lambda$lambda;
    $receiver.onmessage = main$lambda$lambda_0;
    var ws = $receiver;
    (Kotlin.isType(tmp$ = document.getElementById('send'), HTMLInputElement) ? tmp$ : throwCCE()).onclick = main$lambda$lambda_1(ws);
  }
  _.main = main;
  main();
  Kotlin.defineModule('client1-client', _);
  return _;
}));

//# sourceMappingURL=client1-client.js.map
