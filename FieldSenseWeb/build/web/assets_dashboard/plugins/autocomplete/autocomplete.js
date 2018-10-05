$(function(){
  var currencies = [
    { value: 'ABC Pvt Ltd' },
    { value: 'GoldenSpan Pvt Ltd'},
  ];
  
  // setup autocomplete function pulling from currencies[] array
  $('#autocomplete').autocomplete({
    lookup: currencies,
  });
});