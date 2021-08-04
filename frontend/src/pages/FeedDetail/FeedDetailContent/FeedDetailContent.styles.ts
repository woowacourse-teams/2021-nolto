import styled from 'styled-components';

import HighlightedText from 'components/@common/HighlightedText/HighlightedText';
import TextButton from 'components/@common/TextButton/TextButton';
import SOSFlag from 'assets/sosFlag.svg';
import StacksMoreIcon from 'assets/stacksMore.svg';

const Root = styled.div`
  min-width: 54.375rem;
  width: 50vw;
`;

const IntroContainer = styled.div`
  display: flex;
  margin-bottom: 2.5rem;
  width: 100%;
  gap: 3.75rem;
`;

const IconsContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-top: 0.75rem;
`;

const IconWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 0.375rem;
`;

const ThumbnailContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  flex-basis: 14rem;
  flex-grow: 1;
`;

const Thumbnail = styled.div`
  position: relative;
  border-radius: 0.5rem;
  width: 100%;
  overflow: hidden;
  box-shadow: 4px 4px 4px 4px rgba(0, 0, 0, 0.2);

  &::after {
    content: '';
    display: block;
    padding-bottom: 100%;
  }

  & > img {
    position: absolute;
    object-fit: contain;
    height: 100%;
    width: 100%;
    left: 0;
    top: 0;
  }
`;

const SOSFlagIcon = styled(SOSFlag)`
  position: absolute;
  top: -40%;
  right: -10%;
`;

const FeedSummaryContainer = styled.div`
  flex-basis: 40rem;
  flex-grow: 3;
`;

const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
`;

const TitleWrapper = styled.div`
  display: flex;
  align-items: center;
  flex: 1;
  gap: 0.5rem;
`;

const UserWrapper = styled.div`
  display: flex;
  align-items: center;
`;

const UserName = styled.span`
  font-size: 1rem;
`;

const UserImage = styled.img`
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  margin: 0.75rem;
`;

const DetailsContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  margin-top: 1.5rem;
`;

const DetailsPair = styled.div`
  display: flex;
  gap: 1.75rem;
`;

const DetailsKeyWrapper = styled.div`
  display: flex;
  flex-basis: 6.5rem;
  flex-shrink: 0;
`;

const DetailsKey = styled(HighlightedText)`
  font-size: 1rem;
  font-weight: 400;
`;

const DetailsValue = styled.span`
  display: flex;
  font-size: 1rem;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;

  & > a {
    &:hover {
      text-decoration: underline;
    }
  }
`;

export const Tag = styled(TextButton.Rounded)`
  width: fit-content;
  height: 1.5rem;
  padding: 0 0.75rem;
`;

export const StacksMoreButton = styled(StacksMoreIcon)`
  cursor: pointer;
`;

const DescriptionContainer = styled.div`
  & > h3 {
    margin-bottom: 0.75rem;
  }
`;

const Description = styled.pre`
  margin: 4rem auto 0 auto;
  width: 52rem;
  font-size: 1rem;
  line-height: 1.5rem;
  text-align: justify;
  white-space: pre-wrap;
`;

export default {
  Root,
  IntroContainer,
  IconsContainer,
  IconWrapper,
  ThumbnailContainer,
  Thumbnail,
  SOSFlagIcon,
  FeedSummaryContainer,
  TitleContainer,
  TitleWrapper,
  UserWrapper,
  UserName,
  UserImage,
  DetailsContent,
  DetailsPair,
  DetailsKeyWrapper,
  DetailsKey,
  DetailsValue,
  DescriptionContainer,
  Description,
};
