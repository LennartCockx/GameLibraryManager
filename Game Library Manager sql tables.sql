USE [a14_SD405]
GO
/****** Object:  Table [dbo].[Comparison]    Script Date: 7/01/2015 23:09:37 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Comparison](
	[Player] [varchar](50) NOT NULL,
	[gameName] [varchar](50) NOT NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Game_exe_locations]    Script Date: 7/01/2015 23:09:38 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Game_exe_locations](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Game_ID] [bigint] NOT NULL,
	[Location] [varchar](50) NOT NULL,
	[Save_location] [varchar](50) NULL,
 CONSTRAINT [PK_Game_exe_locations] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
