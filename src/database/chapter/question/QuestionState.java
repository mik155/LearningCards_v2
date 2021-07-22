package database.chapter.question;

public enum QuestionState
{
    CORRECT
            {
                @Override
                public boolean correct() {
                    return true;
                }

                @Override
                public boolean inCorrect() {
                    return false;
                }

                @Override
                public boolean noAnswer() {
                    return false;
                }

                public String toString(){return  "☑";};
            },

    IN_CORRECT
            {
                @Override
                public boolean correct() {
                    return false;
                }

                @Override
                public boolean inCorrect() {
                    return true;
                }

                @Override
                public boolean noAnswer() {
                    return false;
                }

                public String toString(){return  "☒";};
            },

    NO_ANSWER
            {
                @Override
                public boolean correct() {
                    return false;
                }

                @Override
                public boolean inCorrect() {
                    return false;
                }

                @Override
                public boolean noAnswer() {
                    return true;
                }

                public String toString(){return  "☐";};
            };

    public abstract boolean correct();
    public abstract boolean inCorrect();
    public abstract boolean noAnswer();
}
