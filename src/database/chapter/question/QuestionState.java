package database.chapter.question;

public enum QuestionState
{
    /**
     * Enum class. It has 3 instance's.
     * CORRECT - is set if question was correct answered
     * IN_CORRECT - is set if question was not answered correct
     * NO_ANSWER - is set if question wasn't answered
     * */
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

                public String toString(){return  "☑";}
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

                public String toString(){return  "☒";}
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

                public String toString(){return  "☐";}
            };

    public abstract boolean correct();
    public abstract boolean inCorrect();
    public abstract boolean noAnswer();
}
