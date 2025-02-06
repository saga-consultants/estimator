package edu.lsu.estimator.tdo;

import java.util.List;

public interface SPIEstimatorMaster {
  TDOMasterRes<Object> check(TDOEstimatorReq paramTDOEstimatorReq, Boolean... paramVarArgs);
  
  TDOMasterRes<TDOCounselor> init(TDOEstimatorReq paramTDOEstimatorReq);
  
  TDOMasterRes<TDOFund> getInitFunds(TDOEstimatorReq paramTDOEstimatorReq);
  
  TDOMasterRes<TDOFund> getAllFunds(TDOEstimatorReq paramTDOEstimatorReq);
  
  TDOMasterRes<TDOCounselor> getAllCounselors(TDOEstimatorReq paramTDOEstimatorReq);
  
  TDOMasterRes<TDOFund> getNewFunds(TDOEstimatorReq paramTDOEstimatorReq, long paramLong);
  
  TDOMasterRes<TDOCounselor> getNewCounselors(TDOEstimatorReq paramTDOEstimatorReq, long paramLong);
  
  TDOMasterRes<TDOStudent> syncNewPickedStuds(TDOEstimatorReq paramTDOEstimatorReq, List<TDOStudent> paramList);
  
  TDOMasterRes<Object> uploadNewPrtsUnpickedStuds(TDOEstimatorReq paramTDOEstimatorReq, List<TDOStudent> paramList);
  
  TDOMasterRes<TDOPrint> syncNewPrts(TDOEstimatorReq paramTDOEstimatorReq, List<TDOPrint> paramList);
  
  TDOMasterRes test(TDOEstimatorReq paramTDOEstimatorReq, List<TDOCounselor> paramList);
}


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\tdo\SPIEstimatorMaster.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */