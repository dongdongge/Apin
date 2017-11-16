
import ConfigIp from '../ConfigIp.js';



export default {

    post: (url,params,successfulCallBack,failCallBack)=>{
        let urls = ConfigIp.host+url;
        let tempParams = '';
        if(params){
            let i = 0;
            for(let key in params){
                let j  = params[key];
                if (j === undefined ){continue;}
                tempParams += i == 0? (key + '=' + params[key]) : ('&' + key + '=' + j);
                i++;
            }
        }
        fetch(urls,{
            method: 'POST',
            headers: {'Content-Type': 'application/json;charset=utf-8',},
            body: tempParams,
        }).then((res)=>res.json())
            .then((result)=>{
                if(result.code>1){
                    successfulCallBack({code:1,message:'',result:result.body});
                }else {
                    failCallBack({code:-1,message:result.message});
                }
            }).catch((err)=>{failCallBack({code:-1,message:err});
        });
    },
    get:(url,successfulCallBack,failCallBack)=>{
        let urls = ConfigIp.host+url;
        console.log(""+urls);
        fetch(urls).then((res)=>{console.log("res"+JSON.stringify(res)),   res.json()})
            .then((result)=>{
                console.log("result"+JSON.stringify(result));
                if(result.code==200){
                    successfulCallBack({code:1,message:'',result:result.body});
                }else {
                    failCallBack({code:-1,message:result.message});
                }
            }).catch((err)=>{
            console.log("err"+err);
            failCallBack({code:-1,message:err});
        });

    }
}