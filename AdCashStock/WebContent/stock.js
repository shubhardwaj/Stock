function sendBid(cid,category) {
	var bidamount = document.getElementById("bidamount").value;
	alert(cid+" "+category+" "+bidamount);
	//window.open("http://localhost:8080/AdCashStock/Bid?biamount="+bidamount+"&cid="+cid+"&category="+category);
	
}