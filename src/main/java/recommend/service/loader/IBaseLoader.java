package recommend.service.loader;

/**
 * Created by ouduobiao on 15/10/30.
 */
public interface IBaseLoader {


    String getEsIndexName();
    String getEsType();
    String getEsIdField();

    void deleteCandidates(Long id, Long recId);

}
