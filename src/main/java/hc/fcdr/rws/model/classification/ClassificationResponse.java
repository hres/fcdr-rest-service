package hc.fcdr.rws.model.classification;

import javax.xml.bind.annotation.XmlRootElement;

import hc.fcdr.rws.domain.Classification;

@XmlRootElement
public class ClassificationResponse
{
    private String classification_number;
    private String classification_name;
    private String classification_type;

    public ClassificationResponse()
    {
        super();
        classification_number = "";
        classification_name = "";
        classification_type = "";
    }

    public ClassificationResponse(final String classificationNumber,
            final String classificationName, final String classificationType)
    {
        super();
        classification_number = classificationNumber;
        classification_name = classificationName;
        classification_type = classificationType;
    }

    public ClassificationResponse(final Classification classification)
    {
        super();
        classification_number = classification.getClassificationNumber();
        classification_name = classification.getClassificationName();
        classification_type = classification.getClassificationType();
    }

    public String getClassification_number()
    {
        return classification_number;
    }

    public void setClassification_number(final String classification_number)
    {
        this.classification_number = classification_number;
    }

    public String getClassification_name()
    {
        return classification_name;
    }

    public void setClassification_name(final String classification_name)
    {
        this.classification_name = classification_name;
    }

    public String getClassification_type()
    {
        return classification_type;
    }

    public void setClassification_type(final String classification_type)
    {
        this.classification_type = classification_type;
    }

    public void setClassificationType(final String classificationType)
    {
        classification_type = classificationType;
    }

}
