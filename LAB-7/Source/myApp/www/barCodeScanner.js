function getBarCodeDataFunction(){
    var url=document.getElementById("barCodeImageUrl").value;
    var barCodeUrl="https://wabr.inliteresearch.com/barcodes?url="+url;
    $.ajax({
        url:barCodeUrl,
        success:function(response){
            var image='';
            image+="<label id='imageLabel'></label><img src='"+url+"' id='barCodeImage'>";
            document.getElementById("image").innerHTML=image;
            var table='';
            table+="<label id='tableLabel'>Barcode Details</label>";
            table+="<tbody>";
            for(k in response.Barcodes[0]){
                table+="<tr><td>"+k+"</td><td>"+response.Barcodes[0][k]+"</td></tr>";
            }
            table+="</tbody>";
            document.getElementById("data").innerHTML=table;
        },
        error:function(){
            console.log("false");
        }
    });
}